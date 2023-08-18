import * as cdk from "aws-cdk-lib";
import { Construct } from "constructs";
import { ChallengeCdkS3Stack } from "./stack/s3-stack";
import { ChallengeApiStack } from "./stack/lambda-stack";
import { ChallengeCdkDynamoStack } from "./stack/dynamo-stack";
import { ChallengeCdkCognitoStack } from "./stack/cognito-stack";
import { Account } from "./config/accounts";
import { SYSTEM_NAME } from "./config/commons";

// import * as sqs from 'aws-cdk-lib/aws-sqs';

export interface ChallengeCdkStackProps extends cdk.StackProps {
  context: Account;
  s3Stack?: ChallengeCdkS3Stack;
  dynamoStack?: ChallengeCdkDynamoStack;
  cognitoStack?: ChallengeCdkCognitoStack;
}

export class ChallengeCdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props: ChallengeCdkStackProps) {
    super(scope, id, props);

    const s3Stack = new ChallengeCdkS3Stack(
      this,
      `${SYSTEM_NAME}-s3Stack`,
      props
    );
    props.s3Stack = s3Stack;

    const dynamoStack = new ChallengeCdkDynamoStack(
      this,
      `${SYSTEM_NAME}-dynamoStack`,
      props
    );
    props.dynamoStack = dynamoStack;

    const cognitoStack = new ChallengeCdkCognitoStack(
      this,
      `${SYSTEM_NAME}-cognitoStack`,
      props
    );
    props.cognitoStack = cognitoStack;

    new ChallengeApiStack(this, `${SYSTEM_NAME}-apiStack`, props);
  }
}
