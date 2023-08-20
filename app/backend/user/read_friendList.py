import json
import boto3
import os
from boto3.dynamodb.conditions import Key

def getTable():
    client = boto3.client('cognito-idp')
    table_name = "team1-ICN-user-table"
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    return table


def lambda_handler(event, context):
    
    table = getTable()
    client = boto3.client('cognito-idp')
    header = event['headers']['Authorization']
    
    result = client.get_user(
            AccessToken = header
    )

    response = table.query(
        KeyConditionExpression=Key('userId').eq(result['Username'])
    )
    
    user = response["Items"][0]
    
    # TODO implement
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
