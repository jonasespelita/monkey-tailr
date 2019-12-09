import {InjectableRxStompConfig} from '@stomp/ng2-stompjs';

export const StompConfig: InjectableRxStompConfig = {
  brokerURL: 'ws://localhost:8080/ws',
  debug: (msg => console.log(new Date(), msg))
};
