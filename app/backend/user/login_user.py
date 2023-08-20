
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
#from accounts import USER_POOL_ID,CLIENT_ID,AWS_PROFILE,cognito_key


def getTable():
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    return table

''' 
def serialNumCheck():
    
    table = getTable()
    userSerialNum  = "user"+str(randint(1,999999)).zfill(6)

    response = table.query(
        KeyConditionExpression=Key('userSerialNum').eq(userSerialNum)
    )

    convert_regular_json = json.loads(json.dumps(response))
    if convert_regular_json["Items"] :
        userSerialNum  = "user"+str(randint(1,999999)).zfill(6)
        return table,userSerialNum
    else : 
        return table,userSerialNum
'''

def decode_verify_token(token):
 
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
            
def cognito_login(password,userId):
    client = boto3.client('cognito-idp')

    try:
        resp=client.initiate_auth(
                 ClientId=CLIENT_ID,
                 AuthFlow='USER_PASSWORD_AUTH',
                 AuthParameters={
                     'USERNAME': userId,
                     'SECRET_HASH': get_secret_hash(userId,CLIENT_ID,cognito_key),
                     'PASSWORD': password,
                  })

        return resp
    
    except client.exceptions.NotAuthorizedException as e:
        return e.response['Error']['Code'] 
    except client.exceptions.UserNotConfirmedException:
        return None, "User is not confirmed"
    except Exception as e:
        return None, e.__str__()


def lambda_handler(event, context):
    client = boto3.client('cognito-idp')
    USERID = event['ID']
    PASSWORD = event['PW']
    response = cognito_login(PASSWORD,USERID)
    access_token = response['AuthenticationResult']['AccessToken']

    try:
        result = client.get_user(
            AccessToken = access_token
        )
        
        return{
            'statusCode':200,
            'body':access_token,
            'headers':{ 'Access-Control-Allow-Origin' : '*' }
        }
    
    
    except botocore.exceptions.ClientError as e :
        error_code = e.response['Error']['Code']
        return{
            'statusCode':500,
            'body':'fail'
        }