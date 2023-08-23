import json
import boto3
import os
from boto3.dynamodb.conditions import Key

def getTable():
    
    table_name = "team1-ICN-user-table"
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    return table

def lambda_handler(event, context):
    
    table = getTable()
    client =boto3.client('cognito-idp')
    #access_token = event["params"]['header']['Authorization']

    #result = client.get_user(
    #        AccessToken = access_token
    #)
    
    response = table.query(
        KeyConditionExpression=Key('userId').eq('Dongmin')
    )
   
    previousPassword = response['Items'][0]['password']
    proposedPassword = event['body-json']['PW']
    nickname = event['body-json']['nickName']
    comment = event['body-json']['Comment']

    '''
    #password 수정
    if not proposedPassword == "NONE" :
        response=update_userPassword(table,response['Items'][0]['userId'],access_token,previousPassword,proposedPassword)
        return {
            'statusCode': response['statusCode'],
            'body': json.dumps('password update!')
        }
    '''
    #nickname 수정
    if not nickname == "NONE" :
        update_userNickname(table,'Dongmin',nickname)
        type = "nickname"
    #infomessage 수정
    if not comment == "NONE" :
        update_userinfoMessage(table,'Dongmin',comment)
        type = "Comment"
    else : 
        return {
            'statusCode': 203,
            'body': json.dumps('정보 변경 없음')
        }
    
    return {
        'statusCode': 200,
        'body': json.dumps('수정 완료')
    }

'''
#패스워드 변경
def update_userPassword(table,userId,access_token,previousPassword,proposedPassword):
    client =boto3.client('cognito-idp')
    try:
        resp = client.change_password(
            PreviousPassword="testdata820@",
            ProposedPassword="testdata821",
            AccessToken = access_token
        )
        
        table.update_item(
            Key={
                'userId': userId
            },
            UpdateExpression="set #password=:data",
            ExpressionAttributeNames={
                "#password":"password"
            },
            ExpressionAttributeValues={
                ":data":password
            }
        )
        
    except client.exceptions.InvalidPasswordException as e:
        
        return {
               "statusCode": 400, 
               "message": "Password should have Caps,\
                          Special chars, Numbers", 
               "data": None}

    except Exception as e:
        return {
                "statusCode": 400, 
                "message": str(e), 
                "data": None
            }
    
    return {
            "statusCode": 200, 
            "message": "Success", 
            "data": None}
'''            
#닉네임 변경
def update_userNickname(table,userId,nickname):
    table.update_item(
        Key={
            'userId': userId
        },
        UpdateExpression="set #nickname=:data",
        ExpressionAttributeNames={
            "#nickname":"nickName"
        },
        ExpressionAttributeValues={
            ":data":nickname
        }
    )

#infomessage 변경
def update_userinfoMessage(table,userId,infomessage):
    table.update_item(
        Key={
            'userId': userId
        },
        UpdateExpression="set #infomessage=:data",
        ExpressionAttributeNames={
            "#infomessage":"infoMessage"
        },
        ExpressionAttributeValues={
            ":data":infomessage
        }
    )
