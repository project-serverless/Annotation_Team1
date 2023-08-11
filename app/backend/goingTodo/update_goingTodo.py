import json
import boto3
import os
from boto3.dynamodb.conditions import Key


def lambda_handler(event, context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    response = table.query(
        KeyConditionExpression= Key('userSerialNum').eq(event['userSerialNum']) & Key('finalDate').eq(event['nowdate']),
        FilterExpression="#status = :statusCode",
        ExpressionAttributeNames={
            '#status':event['status']
        },
        ExpressionAttributeValues={
            ':statusCode':True,
        }
    )
    
    return {
        "statusCode":200,
        "body" : json.dumps(response)
    }
