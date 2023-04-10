package proxy

import spock.lang.Shared
import spock.lang.Specification

class ProxySpec extends Specification {
    @Shared
    private Proxy proxy
    def setup(){
        final Server server = new RealServer()
        proxy  = new Proxy(server)
    }
    def "Handle正常終了確認"() {
        when:
        proxy.handle("1")
        then:
        noExceptionThrown()
    }
    def "authorize 正常終了"(){
        setup:
        final Server server = new RealServer()
        final Proxy proxy  = new Proxy(server)
        when:
        proxy.authorize("1")
        then:
        noExceptionThrown()
    }
    def "authorize 異常終了"(){
        setup:
        final Server server = new RealServer()
        final Proxy proxy  = new Proxy(server)
        when:
        proxy.authorize("5")
        then:
        thrown(Exception)
    }
}
