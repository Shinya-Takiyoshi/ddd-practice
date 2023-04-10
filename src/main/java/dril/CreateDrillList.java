package dril;

import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**DDD設計で実装**/
public class CreateDrillList {
    public static final int MAX_DRILL_SIZE = 100;
    public static final int MIN_DRILL_SIZE = 0;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            /* 初期パラメータ*/
            int plusCnt = sc.nextInt();
            int minusCnt = sc.nextInt();

            //乱数生成
            Random r = new Random();
            HashSet<Drill> plusSetAll = new HashSet<>();
            DrillService plusDrillService = new PlusDrillService();
            do {
                //重複しないプラスリストの生成
                HashSet<Drill> plusSet = plusDrillService.createDrillList(plusCnt, r.nextInt(MAX_DRILL_SIZE), MAX_DRILL_SIZE);
                //全体のリストにマージしていく
                plusSetAll.addAll(plusSet);
            } while (plusSetAll.size() < plusCnt);//生成したプラスのリストが指定された数生成できているか確認

            List<Drill> plusDrillList = new ArrayList<>(plusSetAll);
            // 余分に作成してしまった場合は削除する
            if (plusSetAll.size() > plusCnt) {
                plusDrillList = plusSetAll.stream().limit(plusCnt).collect(Collectors.toList());
            }
            System.out.println("plusList:" + plusDrillList.size());

            //重複しないマイナスリストの生成
            DrillService minudDrillService = new MinusDrillService();
            HashSet<Drill> minusSetAll = new HashSet<>();
            do {
                HashSet<Drill> minusSet = minudDrillService.createDrillList(minusCnt, r.nextInt(MAX_DRILL_SIZE), MIN_DRILL_SIZE);
                minusSetAll.addAll(minusSet);
            } while (minusSetAll.size() < minusCnt);//生成したマイナスのリストが指定された数生成できているか確認

            List<Drill> minusDrillList = new ArrayList<>(minusSetAll);
            // 余分に作成してしまった場合は削除する
            if (minusSetAll.size() > minusCnt) {
                minusDrillList = minusSetAll.stream().limit(minusCnt).collect(Collectors.toList());
            }

            System.out.println("minusList:" + minusDrillList.size());

            //生成した数が要求された数を満たしている場合
            //プラスとマイナスをマージ
            //plusSetAllを全体のリストとする。
            //追加する場合は plusDrillList.addAll(*DrillList);
            plusDrillList.addAll(minusDrillList);

            //リストをシャッフルする
            Collections.shuffle(plusDrillList);
            //表示 && out of Memory対策
            PrintWriter out = new PrintWriter(System.out);
            plusDrillList.stream().limit(plusCnt + minusCnt).forEach(drill -> out.println(drill.getOperand()));
            out.flush();

        }
    }
}

/**
 * ドリルのモデリングクラス
 **/
class Drill {
    Drill(String operand) {
        this.operand = operand;
    }

    public String getOperand() {
        return this.operand;
    }

    private final String operand;
}

class PlusDrillService implements DrillService {
    //プラスリストの生成
    public HashSet<Drill> createDrillList(final int plusCnt, final int drillSize, final int stopSize) {
        if (drillSize <= 0) {
            return new HashSet<>();
        }//0以下の場合、空のリストを返す
        Integer tempDriller = new Integer(drillSize);
        UnaryOperator<Integer> incrementOfPlus = x -> ++x;
        BiFunction<Integer, Integer, Integer> rightCalc = (y, z) -> y - z;
        BiPredicate<Integer, Integer> isCounterMax = (cnt, inputCnt) -> cnt < inputCnt;
        BiPredicate<Integer, Integer> isCalcMax = (cnt, inputCnt) -> cnt < inputCnt; //左右どちらかの値が100になったら終了

        HashSet<Drill> drillList = new HashSet<>();
        int count = 0;
        while (isCounterMax.test(count, plusCnt)) {
            //左の式を作る
            String operandLeft = tempDriller.toString();
            //右の式を作る
            String operandRight = rightCalc.apply(stopSize, tempDriller).toString();
            //式を作成してリストに追加する
            drillList.add(new Drill(convertDrillString(operandLeft, operandRight)));
            //左のインクリメント
            tempDriller = incrementOfPlus.apply(tempDriller);
            //生成数のカウント
            count++;
            if (isCalcMax.test(stopSize, tempDriller)) break;
        }
        return drillList;
    }

    /**
     * 式を作成する
     **/
    public String convertDrillString(String operandLeft, String operandRight) {
        StringBuilder builder = new StringBuilder();
        builder.append(operandLeft);
        builder.append(" " + Operator.PLUS.getOperator() + " ");
        builder.append(operandRight);
        builder.append(" =");
        return builder.toString();
    }
}

class MinusDrillService implements DrillService {
    //マイナスリストの生成
    public HashSet<Drill> createDrillList(final int minusCnt, final int drillSize, final int stopSize) {
        if (drillSize <= 0) {
            return new HashSet<>();
        }//0以下の場合、空のリストを返す
        //左の式は固定のため1度だけ作成
        String operandLeft = String.valueOf(drillSize);

        Integer tempDriller = new Integer(drillSize);
        UnaryOperator<Integer> incrementOfMinus = x -> --x;
        BiPredicate<Integer, Integer> isCounterMax = (cnt, inputCnt) -> cnt < inputCnt;
        Predicate<Integer> isCalcMin = (inputCnt) -> inputCnt == stopSize; //右の式が0になったら終了


        HashSet<Drill> drillList = new HashSet<>();
        int count = 0;
        while (isCounterMax.test(count, minusCnt)) {
            //右の式を作る
            String operandRight = tempDriller.toString();
            //式を作成してリストに追加する
            drillList.add(new Drill(convertDrillString(operandLeft, operandRight)));
            //左のインクリメント
            tempDriller = incrementOfMinus.apply(tempDriller);
            //生成数のカウント
            count++;
            if (isCalcMin.test(tempDriller)) break;
        }
        return drillList;
    }

    /**
     * 式を作成する
     **/
    public String convertDrillString(String operandLeft, String operandRight) {
        StringBuilder builder = new StringBuilder();
        builder.append(operandLeft);
        builder.append(" " + Operator.MINUS.getOperator() + " ");
        builder.append(operandRight);
        builder.append(" =");
        return builder.toString();
    }
}

interface DrillService {
    HashSet<Drill> createDrillList(final int Cnt, final int drillSize, final int stopSize);

    String convertDrillString(String operandLeft, String operandRight);
}

enum Operator {
    PLUS("+"),
    MINUS("-");

    Operator(String s) {
        this.operator = s;
    }

    private String operator;

    public String getOperator() {
        return this.operator;
    }
}

