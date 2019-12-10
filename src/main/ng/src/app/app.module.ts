import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from '@stomp/ng2-stompjs';
import {MessagesComponent} from './messages/messages.component';
import {AppConfigService} from 'src/app/services/app-config.service';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';

const StompConfigFactory = (appConfig: AppConfigService) => {
  return {
    brokerURL: appConfig.env.brokerUrl,
    debug: msg => console.log(new Date(), msg)
  };
};

@NgModule({
  declarations: [
    AppComponent,
    MessagesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    AppConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: (appConfig: AppConfigService) => {
        return () => appConfig.loadAppConfig()
      },
      multi: true,
      deps: [AppConfigService]
    },
    {
      provide: InjectableRxStompConfig,
      deps: [AppConfigService],
      useFactory: StompConfigFactory
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    }],
  bootstrap: [AppComponent]
})
export class AppModule {}
