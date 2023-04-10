package dril

import spock.lang.Specification

class PlusDrillServiceSpec extends Specification {
    def "CreateDrillList"() {
        given:
        PlusDrillService plusDrillService = new PlusDrillService();
        expect:
        plusDrillService.createDrillList(plusCnt,drillSize,stopSize).size() == expected
        where:
        plusCnt |drillSize|stopSize ||expected
        1 | 0 | 100 | 0
        1 | 35 | 100 | 1
        50 | 40 | 100 | 50
        50 | 100 | 100 | 1
    }

    def "ConvertDrillString"() {
    }
}
