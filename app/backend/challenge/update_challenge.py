import json
import boto3
import os
from boto3.dynamodb.conditions import Key


def lambda_handler(event, context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    #update todoSerialNum, level, status, date
    todoSerialNum = event['todoSerialNum']
    Level = event['Level']
    status = event['status']
    date=event['date']

    if Level == "Third":
        table.update_item(
            Key={
            'todoSerialNum': todoSerialNum
            },
            UpdateExpression="set #status=:statusCode",
            ExpressionAttributeNames={
                "#status":"status"
            },
            ExpressionAttributeValues={
                ":statusCode":True
            }
        )

    response = table.update_item(
        Key={
            'todoSerialNum': todoSerialNum
        },
        UpdateExpression="set #date.#level=:data",
        ExpressionAttributeNames={
            "#date":"date",
            "#level":Level
        },
        ExpressionAttributeValues={
            ":data":{
                "date":date,
                "status":status
            }
        }
    )

    return {
        "statusCode":200,
        "body" : json.dumps("Success")
    }