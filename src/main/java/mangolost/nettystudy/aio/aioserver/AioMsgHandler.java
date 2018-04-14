package mangolost.nettystudy.aio.aioserver;

/**
 *
 */
public class AioMsgHandler {

    public AioMsgHandler() {

    }

    /**
     *
     * @param msg
     * @return
     */
    public Object doHandle(String msg){
        //把msg倒转文字返回
        String result = new StringBuilder(msg).reverse().toString();
        return result;
    }
}
