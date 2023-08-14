import json
import boto3
import os
from random import randint
from boto3.dynamodb.conditions import Key

# 테이블 정보 가져오기
def getTable():
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    return table

#userSerialNum 중복 검사 
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

def lambda_handler(event, context):

    table,userSerialNum = serialNumCheck()

    nickName=event['nickName']
    infoMessage = event["infoMessage"]
    password=event["password"]
    userId=event['userId']

    item = {
        "userSerialNum": userSerialNum,
        "infoMessage":infoMessage,
        "nickName": nickName,
        "password":password,
        "userId":userId
    }

    table.put_item(Item=item)

    return {
        'statusCode': 200,
        'body': json.dumps(f'{nickName} saved!')
    }