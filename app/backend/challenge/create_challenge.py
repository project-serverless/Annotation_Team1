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
    todoSerialNum = "todo"+str(randint(1,999999)).zfill(6)

    response = table.query(
        KeyConditionExpression=Key('todoSerialNum').eq(todoSerialNum)
    )

    convert_regular_json = json.loads(json.dumps(response))
    if not convert_regular_json["Items"] :
        todoSerialNum = "todo"+str(randint(1,999999)).zfill(6)
        return table,todoSerialNum
    else : 
        return table,todoSerialNum
    
#create todo 
def lambda_handler(event, context):
    
    userId = event['userId']
    table,todoSerialNum = serialNumCheck()

    item = {
        'userId': userId,
        'finalDate':event['finalDate'],
        'todoSerialNum':todoSerialNum,
        'date':
                {
                    'First':{
                        'date':event['date1'],
                        'status':False
                    },
                    'Second':{
                        'date':event['date2'],
                        'status':False
                    },
                    'Third':{
                        'date':event['date3'],
                        'status':False
                    }
                },
        'goalContent':event['goalContent'],
        'month':event['month'],
        'status':event['status']
    }

    table.put_item(Item=item)
    
    return {
        'statusCode': 200,
        'body': json.dumps(f'{userId} create!')
    }