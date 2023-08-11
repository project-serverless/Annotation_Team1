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

#todoSerialNum 중복 검사 
def serialNumCheck():
    
    table = getTable()

    serialNum  = "user"+str(randint(1,999999)).zfill(6)

    response = table.query(
        KeyConditionExpression=Key('serialNum').eq(serialNum)
    )

    convert_regular_json = json.loads(json.dumps(response))
    if convert_regular_json["Items"] :
        serialNum  = "user"+str(randint(1,999999)).zfill(6)
        return table,serialNum
    else : 
        return table,serialNum

def lambda_handler(event, context):

    table,serialNum = serialNumCheck()

    nickName=event['nickName']
    infoMessage = event["infoMessage"]
    password=event["password"]
    userId=event['userId']

    item = {
        "serialNum": serialNum,
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