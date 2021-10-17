package redis;

public class RedisRecv {

	private JdHandleMessageService handleService;
	
	public void recvMsg(String msg, String channel) {
		log.info("-----------------------------recvMsg: {}", channel);
		//������ͨ��Ϣ 
		if (channel.startsWith(Constant.handle_message_queue_prefix)) { 
			String typeStr = channel.substring(channel.lastIndexOf('_') + 1); 
			int type = Integer.parseInt(typeStr);
			log.info("��Ϣ����type:��{}��", type);
			switch (type) { case 1: log.info("��������Ϣ type:{}", type);
			handleService.handleSplit(msg);
			break;
			case 5: log.info("��ȡ��Ͷ��Ϣ type:{}", type);
			handleService.handleReceipt(msg); break; 
	log.info("��{}��δ����ش�����", type); } } }
	}
}
