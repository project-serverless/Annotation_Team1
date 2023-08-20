import json
from datetime import datetime
import dateutil.tz
import boto3
from boto3.dynamodb.conditions import Key

def set_table(user,date,usertable,statusCode):
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table("team1-ICN-challenge-table")
    
    response = table.query(
        KeyConditionExpression=Key('todoSerialNum').eq(user['Goal'])
    )
    
    finalDate = response['Items'][0]['finalDate']
    day = finalDate.split('-')[2]
    
    if int(day)-int(date.day) == 2:
        Level = "day1"
    elif int(day)-int(date.day) == 1:
        Level = "day2"
    elif int(day)-int(date.day) == 0:
        Level = "day3"
    
    table.update_item(
        Key={
            'todoSerialNum': user['Goal']
        },
        UpdateExpression="set #goalArr.#level=:data",
        ExpressionAttributeNames={
            "#goalArr":"goalArr",
            "#level":Level
        },
        ExpressionAttributeValues={
            ":data":{
                "date":date.strftime('%Y-%m-%d'),
                "status":statusCode
            }
        }
    )
    
    index = int(Level[3])-1
    list = user['GoalArr']
    list[index]=True
    
    usertable.update_item(
        Key={
            'userId': user['userId']
        },
        UpdateExpression="set #GoalArr=:data",
        ExpressionAttributeNames={
            "#GoalArr":"GoalArr"
        },
        ExpressionAttributeValues={
            ":data":list
        }
    )

def lambda_handler(event, context):
    client = boto3.client('cognito-idp')
    table_name = "team1-ICN-user-table"
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    header = event['headers']['Authorization']
    asia = dateutil.tz.gettz('Asia/Seoul')
    date = datetime.now(tz=asia)
    
    result = client.get_user(
            AccessToken = header
    )
    
    response = table.query(
        KeyConditionExpression=Key('userId').eq('testdata222')
    )
    user = response["Items"][0]
    
    if date.hour == 0 and date.minute == 0 and date.second == 0:
        table.update_item(
            Key={
                'userId': user['userId']
            },
            UpdateExpression="set #todaysuccess=:statusCode, #GoalArr",
            ExpressionAttributeNames={
                "#todaysuccess":"todaysuccess"
            },
            ExpressionAttributeValues={
                ":statusCode":False
            }
        )
    else :
        table.update_item(
            Key={
                'userId': user['userId']
            },
            UpdateExpression="set #todaysuccess=:statusCode",
            ExpressionAttributeNames={
                "#todaysuccess":"todaysuccess"
            },
            ExpressionAttributeValues={
                ":statusCode":True
            }
        )
        
        set_table(user,date,table,True)
       
    
    return{
        "body":"Hello Lambda"
    }

