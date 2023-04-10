package dril

import spock.lang.Specification

class MinusDrillServiceSpec extends Specification {
    def "要求値が#minusCntの時でランダム値が#drillSizeの時は#expectedのリストを返す"() {
        given:
        MinusDrillService minusDrillService = new MinusDrillService();
        expect:
        minusDrillService.createDrillList(minusCnt,drillSize,stopSize).size() == expected
        where:
        minusCnt |drillSize|stopSize ||expected
        1 | 0 | 0 | 0
        1 | 35 | 0 | 1
        50 | 40 | 0 | 40
        50 | 100 | 0 | 50
    }

    def "ConvertDrillString"() {
    }
}
