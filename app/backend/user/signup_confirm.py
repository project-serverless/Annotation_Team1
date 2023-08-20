
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




def getTable():
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    return table

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
 

def cognito_confirm_signup(password,name,code):
    client = boto3.client('cognito-idp')
    
    try:
        resp = client.confirm_sign_up(
            ClientId=CLIENT_ID,
            SecretHash=get_secret_hash(name, CLIENT_ID,cognito_key),
            Username=name,
            ConfirmationCode=code,
            ForceAliasCreation=False
        )

        return {"success": True, "message": f"Signup confirmed for user: {name}", "response": resp}

    except client.exceptions.UserNotFoundException:
        return {"success": False, "message": "Username doesnt exists"}
    except client.exceptions.CodeMismatchException:
        return {"success": False, "message": "Invalid Verification code"}
    except client.exceptions.NotAuthorizedException:
        return {"success": False, "message": "User is already confirmed"}
    except Exception as e:
        return {"success": False, "message": f"Unknown error {e.__str__()} "}
    
def lambda_handler(event, context):
    client = boto3.client('cognito-idp')

    USERID = event['ID']
    PASSWORD =event['PW']
    NICKNAME = event['nickName']
    INFOMESSAGE = event['infoMessage']
    CODE = event['Code']
    response= cognito_confirm_signup(PASSWORD,USERID, CODE)
    
    try:

        table = getTable()

        item = {
            "userId":USERID,
            "password":PASSWORD,
            "nickName": NICKNAME,
            "infoMessage":INFOMESSAGE,
            "SuccessGoal":0,
            "ContinueGoal":0,
            "FriendCnt":0,
            "Goal":"NONE", #진행중인 goal todoSerialNum
            "GoalArr":[False,False,False],
            "Friends":["NONE"], #친구 userId
            "TodaySuccess":False
        }

        table.put_item(Item=item)
        
        return{
            'statusCode':200,
            'body':json.dumps(response['message'])
        }
    
    except botocore.exceptions.ClientError as e :
        error_code = e.response['Error']['Code']
        return{
            'statusCode':500,
            'body':error_code
        }
  