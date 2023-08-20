import json
import boto3
import os
from boto3.dynamodb.conditions import Key
import random


def lambda_handler(event, context):
    client = boto3.client('cognito-idp')
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    goalTable = dynamodb.Table('team1-ICN-challenge-table')
    header = event['headers']['Authorization']
    
    result = client.get_user(
            AccessToken = header
    )

    response = table.query(
        KeyConditionExpression=Key('userId').eq(result['Username'])
    )
    import json
import boto3
import os
from boto3.dynamodb.conditions import Key
import random


def lambda_handler(event, context):
    client = boto3.client('cognito-idp')
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    goalTable = dynamodb.Table('team1-ICN-challenge-table')
    header = event['headers']['Authorization']
    
    #accesstoken -> userId
    result = client.get_user(
            AccessToken = header
    )
    
    #userId -> user 테이블 조회
    response = table.query(
        KeyConditionExpression=Key('userId').eq(result['Username'])
    )
    user = response["Items"][0]
    
    goal1 = goalTable.query(
        KeyConditionExpression=Key('todoSerialNum').eq(user['Goal'])
    )
    
    if not goal1['Items'] :
        Goal="NONE"
    else:
        Goal = goal1["Items"][0]['goalContent']
    
    
    #랜덤 friend userId 추출. 없으면 NONE : string
    randomFriend = random.choice(user["Friends"])
    #user가 friend가 있을 경우 friendtable 조회 -> friend goal 조회
    if not randomFriend == "NONE":
        friend = table.query(
            KeyConditionExpression=Key('userId').eq(randomFriend)
        )
    
        goal2 = goalTable.query(
            KeyConditionExpression=Key('todoSerialNum').eq(friend["Items"][0]['Goal'])
        )
        
        FriendName = friend["Items"][0]['nickName']
        FriendGoal = goal2["Items"][0]['goalContent']
        FriendGoalArr = friend["Items"][0]['GoalArr']
    else : 
        FriendName = "NONE"
        FriendGoal = "NONE"
        FriendGoalArr = "NONE"
        
        
    response_data = {
        "Name":user["nickName"],
        "SuccessGoal":int(user["SuccessGoal"]),
        "ContinueGoal":int(user["ContinueGoal"]),
        "FriendCnt":int(user["FriendCnt"]),
        "Goal":Goal, #현재 진행중인 챌린지 이름
        "GoalArr":user['GoalArr'],
        "FriendName":FriendName, #친구 이름. 랜덤으로 줌
        "FriendGoal":FriendGoal, #친구 목표. 
        "TodaySuccess":user['TodaySuccess'],
        "FriendGoalArr":FriendGoalArr #친구 진행중인 챌린지 상황황
    }
    
    return {
        'statusCode': 200,
        'body':json.dumps(response_data,ensure_ascii=False)
    }


#항목 개수 : len(convert_regular_json["Items"][0][type])
#항목 상세정보 : len(convert_regular_json["Items"][0][type]

    user = response["Items"][0]
    
    randomFriend = random.choice(user["friends"])
    
    friend = table.query(
        KeyConditionExpression=Key('userId').eq(randomFriend)
    )
    
    goal1 = goalTable.query(
        KeyConditionExpression=Key('todoSerialNum').eq(user['goal'])
    )
    
    goal2 = goalTable.query(
        KeyConditionExpression=Key('todoSerialNum').eq(friend["Items"][0]['goal'])
    )
        
    
    response_data = {
        "Name":user["nickName"],
        "SuccessGoal":int(user["successgoal"]),
        "ContinueGoal":int(user["continuegoal"]),
        "FriendCnt":int(user["friendcnt"]),
        "Goal":goal1["Items"][0]['goalContent'], #현재 진행중인 챌린지 이름
        "FriendName":friend["Items"][0]['nickName'], #친구 이름. 랜덤으로 줌
        "FriendGoal":goal2["Items"][0]['goalContent'], #친구 목표. 
        "TodaySuccess":user['todaysuccess'],
        "goalArr":user['goalarr'],
        "friendgoalArr":friend["Items"][0]['goalarr']
        
    }
    
    return {
        'statusCode': 200,
        'body':json.dumps(response_data,ensure_ascii=False)
    }

#항목 개수 : len(convert_regular_json["Items"][0][type])
#항목 상세정보 : len(convert_regular_json["Items"][0][type]
