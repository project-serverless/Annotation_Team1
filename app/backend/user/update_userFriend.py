import json
import json
import boto3
import os
from random import randint
from boto3.dynamodb.conditions import Key

def get_FriendCnt(table,userId):
    
    response = table.query(
        KeyConditionExpression=Key('userId').eq(userId)
    )
    
    return int(response["Items"][0]["FriendCnt"])


def lambda_handler(event, context):
    table_name = 'team1-ICN-user-table'
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    userId = event['body-json']['userId']
    FriendId=event['body-json']['FriendId']
    userCnt = get_FriendCnt(table,userId)
    friendCnt = get_FriendCnt(table,FriendId)
    
    #user 친구 추가
    table.update_item(
        Key={
            'userId':userId
        },
        UpdateExpression='set #Friends= list_append(#Friends,:friend),#FriendCnt=:FriendCnt',
        ExpressionAttributeNames={
            '#Friends':'Friends',
            '#FriendCnt':'FriendCnt'
        },
        ExpressionAttributeValues={
            ':friend':[FriendId],
            ':FriendCnt':int(userCnt+1)
        }
    )
    
    #Friend 친구 추가
    table.update_item(
        Key={
            'userId':FriendId
        },
        UpdateExpression='set #Friends= list_append(#Friends,:friend),#FriendCnt=:FriendCnt',
        ExpressionAttributeNames={
            '#Friends':'Friends',
            '#FriendCnt':'FriendCnt'
        },
        ExpressionAttributeValues={
            ':friend':[userId],
            ':FriendCnt':int(friendCnt+1)
        }
    )
    
    return {
        'statusCode': 200
    }