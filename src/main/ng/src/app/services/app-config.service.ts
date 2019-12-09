import {Injectable, Injector} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "src/environments/environment";

@Injectable({
  providedIn: 'root'
})
/**
 * Service for initializing dynamic application configuration.
 *
 * Pattered from https://www.technouz.com/4746/how-to-use-run-time-environment-variables-in-angular/
 */
export class AppConfigService {

  env: any = {};
  activeProfiles: Array<string>;

  constructor(private injector: Injector) {
  }


  loadAppConfig() {
    // since running while bootstrap, DI is not yet ready. Manually injecting
    const http = this.injector.get(HttpClient);

    return http.get('/frontend/config')
      .toPromise()
      .then(data => {
        // populate this object with configurations
        Object.keys(data)
          .forEach(key => this.env[key] = data[key]);
      }).catch(
        () => {
          console.warn("failed to call /frontend/config. falling back to environment.ts!");
          Object.keys(environment)
            .forEach(key => this.env[key] = environment[key]);
        }
      );
  }
}
