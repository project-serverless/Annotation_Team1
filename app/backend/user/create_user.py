import boto3
import botocore.exceptions
import hmac
import hashlib
import base64
import json
import os
from pprint import pprint
import json
import boto3
import os
from random import randint
from boto3.dynamodb.conditions import Key

def decode_verify_token(token):
   
    # build the URL where the public keys are
    jwks_url = 'https://cognito-idp.{}.amazonaws.com/{}/.well-known/jwks.json'.format(
                        'ap-northeast-2_LmHeIjgVm',
                        USER_POOL_ID)
    # get the keys
    jwks = requests.get(jwks_url).json()
    decoded_token = jwt.decode(token, key=jwks, options={'verify_aud':False, 'verify_signature':False})
#     pprint(decoded_token)
    return decoded_token
    
def get_secret_hash(username, cognito_client_id,cognito_key):
    msg = username + cognito_client_id
    dig = hmac.new(str(cognito_key).encode('utf-8'), 
        msg = str(msg).encode('utf-8'), digestmod=hashlib.sha256).digest()
    d2 = base64.b64encode(dig).decode()
    return d2
    
    
def cognito_signup(email, password, name):

    client =boto3.client('cognito-idp')
    
    try:
        resp = client.sign_up(
            ClientId = CLIENT_ID,
            SecretHash=get_secret_hash(name, CLIENT_ID,cognito_key),
            Username=name,
            Password=password,
            UserAttributes=[
                {
                    "Name":"email",
                    "Value":email
                }
            ]
        )
        
    except client.exceptions.UsernameExistsException as e:
        return {
               "success": False, 
               "message": "This username already exists", 
               "data": None}
    except client.exceptions.InvalidPasswordException as e:
        
        return {
               "success": False, 
               "message": "Password should have Caps,\
                          Special chars, Numbers", 
               "data": None}
    except client.exceptions.UserLambdaValidationException as e:
        return {
               "success": False, 
               "message": "Email already exists", 
               "data": None}
    
    except Exception as e:
        return {
                "success": False, 
                "message": str(e), 
                "data": None
            }
    
    return {
            "success": True, 
            "message": "Please confirm your signup, \
                        check Email for validation code", 
            "data": None}


def lambda_handler(event, context):
    
    client = boto3.client('cognito-idp')

    USERID = event['ID']
    PASSWORD =event['PW']
    EMAIL = event['Email']

    response = cognito_signup(EMAIL, PASSWORD, USERID)

    return{
        'headers':{'Access-Control-Allow-Origin': '*'},
        'statusCode':200,
        'body':json.dumps({'resp':response})
    }
    