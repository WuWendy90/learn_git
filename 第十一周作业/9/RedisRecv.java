package redis;

public class RedisRecv {

	private JdHandleMessageService handleService;
	
	public void recvMsg(String msg, String channel) {
		log.info("-----------------------------recvMsg: {}", channel);
		//处理普通消息 
		if (channel.startsWith(Constant.handle_message_queue_prefix)) { 
			String typeStr = channel.substring(channel.lastIndexOf('_') + 1); 
			int type = Integer.parseInt(typeStr);
			log.info("消息处理type:【{}】", type);
			switch (type) { case 1: log.info("订单拆单信息 type:{}", type);
			handleService.handleSplit(msg);
			break;
			case 5: log.info("获取妥投信息 type:{}", type);
			handleService.handleReceipt(msg); break; 
	log.info("该{}暂未有相关处理方法", type); } } }
	}
}
