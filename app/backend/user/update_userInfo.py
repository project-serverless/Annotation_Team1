import json
import boto3
import os


def lambda_handler(event, context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    type = event['updateAttrType']
    if(type == "userId"):
        return {
            'statusCode': 200,
            'body': json.dumps(f'{type} fail!')
        }
    else:
        serialNum = event['serialNum']
        content = event['content']
    
        table.update_item(
            Key={
                'serialNum':serialNum
            },
            UpdateExpression='set #updateType= :content',
            ExpressionAttributeNames={
                '#updateType':type
            },
            ExpressionAttributeValues={
                ':content':[content]
            }
        )
    
        return {
            'statusCode': 200,
            'body': json.dumps(f'{serialNum}{type}{content}update!')
        }
    
    