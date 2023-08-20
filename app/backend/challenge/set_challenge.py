import json
import boto3
import os
from boto3.dynamodb.conditions import Key
from datetime import datetime,timedelta
import dateutil.tz
from random import randint 

def serialNumCheck(table):
    
    todoSerialNum = "todo"+str(randint(1,999999)).zfill(6)

    response = table.query(
        KeyConditionExpression=Key('todoSerialNum').eq(todoSerialNum)
    )

    convert_regular_json = json.loads(json.dumps(response))
    if not convert_regular_json["Items"] :
        todoSerialNum = "todo"+str(randint(1,999999)).zfill(6)
        return todoSerialNum
    else : 
        return todoSerialNum
        
def user_set(result,Goal) :
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('team1-ICN-user-table')
    
    table.update_item(
        Key={
            'userId': result['Username']
        },
        UpdateExpression="set #Goal=:goal, #GoalArr=:goalStatus",
        ExpressionAttributeNames={
            "#Goal":"Goal",
            "#GoalArr":"GoalArr"
        },
        ExpressionAttributeValues={
            ":goal":Goal,
            ":goalStatus":[False,False,False]
        }
    )

def lambda_handler(event, context):
    client = boto3.client('cognito-idp')
    dynamodb = boto3.resource('dynamodb')
    
    table = dynamodb.Table('team1-ICN-challenge-table') 
    asia = dateutil.tz.gettz('Asia/Seoul')
    day1 = datetime.now(tz=asia)
    
    access_token = event["params"]['header']['Authorization']
    Goal = event['body-json']['Goal']
    todoSerialNum = serialNumCheck(table)
    day2 = day1+timedelta(days=1)
    day3 = day2+timedelta(days=1)
    
    result = client.get_user(
            AccessToken = access_token
    )
    
    item = {
        'userId' : result['Username'],
        'finalDate':day3.strftime('%Y-%m-%d'),
        'todoSerialNum':todoSerialNum,
        'goalArr':
            {
                'day1':{
                    'date':day1.strftime('%Y-%m-%d'),
                    'status':False
                },
                'day2':{
                    'date':day2.strftime('%Y-%m-%d'),
                    'status':False
                },
                'day3':{
                    'date':day3.strftime('%Y-%m-%d'),
                    'status':False
                }
            },
        'goalConent':Goal,
        'month':str(day1.month),
        'status':False
    }
    table.put_item(Item=item)
    user_set(result,Goal)
    
    # TODO implement
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
