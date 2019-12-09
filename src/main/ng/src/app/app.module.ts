import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from '@stomp/ng2-stompjs';
import {StompConfig} from 'src/app/stomp.config';
import {MessagesComponent} from './messages/messages.component';
import {AppConfigService} from 'src/app/services/app-config.service';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';

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
    {
      provide: InjectableRxStompConfig,
      useValue: StompConfig
    },
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
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    }],
  bootstrap: [AppComponent]
})
export class AppModule {}
