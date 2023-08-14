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
import { Todo3DaysCdkStackProps } from "../hello-cdk-stack";
import { SYSTEM_NAME } from "../config/commons";

export class Todo3DaysCdkDynamoStack extends cdk.Stack {
  public User: ITable;
  public goingTodo: ITable;

  constructor(scope: Construct, id: string, props: Todo3DaysCdkStackProps) {
    super(scope, id, props);

    this.User = new Table(this, `${SYSTEM_NAME}-user-table`, {
      tableName: `${getAccountUniqueName(props.context)}-user-table`,
      partitionKey: {
        name: "userSerialNum",
        type: AttributeType.STRING,
      },
      billingMode: BillingMode.PAY_PER_REQUEST,
    });

    this.goingTodo = new Table(this, `${SYSTEM_NAME}-goingtodo-table`, {
      tableName: `${getAccountUniqueName(props.context)}-goingtodo-table`,
      partitionKey: {
        name: "todoSerialNum",
        type: AttributeType.STRING,
      },
      billingMode: BillingMode.PAY_PER_REQUEST,
    });
  }
}
