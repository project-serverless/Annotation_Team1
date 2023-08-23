import json
import json
import boto3
import os
from random import randint
from boto3.dynamodb.conditions import Key

def search_goal(dynamodb,Goal):
    table = dynamodb.Table('team1-ICN-challenge-table')
    
    if Goal == "NONE":
        return Goal
    else :
        response = table.query(
            KeyConditionExpression=Key('todoSerialNum').eq(Goal)
        )
        return response["Items"][0]["GoalContent"]
        
def lambda_handler(event, context):
    
    table_name = 'team1-ICN-user-table'
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    #친구 id 이용해 테이블 호출
    response = table.query(
            KeyConditionExpression=Key('userId').eq(event['body-json']['userId'])
    )
    Friend = response["Items"]
    
    #userId조회됨
    if Friend:
        Friend = response["Items"][0]
        Goal = search_goal(dynamodb,Friend["Goal"])
        
        if Friend["SuccessGoal"] == 0 or Friend["TotalGoal"] == 0 :
            SuccessPercent = 0
        else :
            SuccessPercent = (int(Friend["SuccessGoal"]) * 100)//int(Friend["TotalGoal"])
        
        FriendDetails = {
            "Name":Friend["nickName"],
            "Comment":Friend["infoMessage"],
            "SuccessGoal":int(Friend["SuccessGoal"]),
            "ContinueGoal":int(Friend["ContinueGoal"]),
            "FriendCnt":int(Friend["FriendCnt"]),
            "SuccessPercent":int(SuccessPercent),
            "Goal":Goal,
            "GoalArr":Friend["GoalArr"],
            "TodaySuccess":Friend["TodaySuccess"]
        }
        
        return {
            'headers':{ 'Access-Control-Allow-Origin' : '*' },
            'statusCode': 200,
            'body': FriendDetails
        }
    #조회되지 않음    
    else:
        return {
            'headers':{ 'Access-Control-Allow-Origin' : '*' },
            'statusCode': 400,
            'body':"user가 존재하지 않습니다."
        }
