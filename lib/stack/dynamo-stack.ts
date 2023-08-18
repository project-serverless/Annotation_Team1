import * as cdk from "aws-cdk-lib";
import { Construct } from "constructs";
import * as s3 from "aws-cdk-lib/aws-s3";
import { getAccountUniqueName, Account } from "../config/accounts";
import {
  AttributeType,
  BillingMode,
  ITable,
  Table,
} from "aws-cdk-lib/aws-dynamodb";
import { ChallengeCdkStackProps } from "../hello-cdk-stack";
import { SYSTEM_NAME } from "../config/commons";

export class ChallengeCdkDynamoStack extends cdk.Stack {
  public User: ITable;
  public challenge: ITable;

  constructor(scope: Construct, id: string, props: ChallengeCdkStackProps) {
    super(scope, id, props);

    this.User = new Table(this, `${SYSTEM_NAME}-user-table`, {
      tableName: `${getAccountUniqueName(props.context)}-user-table`,
      partitionKey: {
        name: "userId",
        type: AttributeType.STRING,
      },
      billingMode: BillingMode.PAY_PER_REQUEST,
    });

    this.challenge = new Table(this, `${SYSTEM_NAME}-challenge-table`, {
      tableName: `${getAccountUniqueName(props.context)}-challenge-table`,
      partitionKey: {
        name: "todoSerialNum",
        type: AttributeType.STRING,
      },
      billingMode: BillingMode.PAY_PER_REQUEST,
    });
  }
}
