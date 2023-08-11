import * as cdk from "aws-cdk-lib";
import { Construct } from "constructs";
import { Todo3DaysCdkS3Stack } from "./stack/s3-stack";
import { Todo3DaysApiStack } from "./stack/lambda-stack";
import { Todo3DaysCdkDynamoStack } from "./stack/dynamo-stack";
import { Account } from "./config/accounts";
import { SYSTEM_NAME } from "./config/commons";
// import * as sqs from 'aws-cdk-lib/aws-sqs';

export interface Todo3DaysCdkStackProps extends cdk.StackProps {
  context: Account;
  s3Stack?: Todo3DaysCdkS3Stack;
  dynamoStack?: Todo3DaysCdkDynamoStack;
}

export class Todo3DaysCdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props: Todo3DaysCdkStackProps) {
    super(scope, id, props);

    const s3Stack = new Todo3DaysCdkS3Stack(
      this,
      `${SYSTEM_NAME}-s3Stack`,
      props
    );
    props.s3Stack = s3Stack;

    const dynamoStack = new Todo3DaysCdkDynamoStack(
      this,
      `${SYSTEM_NAME}-dynamoStack`,
      props
    );
    props.dynamoStack = dynamoStack;

    new Todo3DaysApiStack(this, `${SYSTEM_NAME}-apiStack`, props);
  }
}
