package guxiangfly

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.api.scala._

object DataSetWcApp {
  def main(args: Array[String]): Unit = {
    val env:ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment;
    val txtDataSet:DataSet[String] = env.readTextFile("/Users/mtdp/dev/ideaworkspace/guxiangwork/technology-study/data/BUILDING.txt");
    //val value = txtDataSet.flatMap(_.split(" ")).map((_,1)).groupBy(0).sum(1)
    val value: AggregateDataSet[(String, Int)] = txtDataSet.flatMap((_: String).split(" ")).map((_, 1)).groupBy(0).sum(1)
    value.print();
  }
}
