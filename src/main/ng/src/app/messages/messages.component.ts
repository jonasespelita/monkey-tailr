import {AfterViewChecked, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Subscription} from 'rxjs';
import {AppConfigService} from 'src/app/services/app-config.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit, OnDestroy, AfterViewChecked {
  private testSub: Subscription;
  private fileSubs: Subscription[] = [];
  private curSelFileKey: string;

  @ViewChild('scrollMe', {static: false}) private scrollMe: ElementRef;

  constructor(private stompService: RxStompService,
              private configService: AppConfigService) { }

  private _curLog: string = "";

  get curLog(): string {
    return this._curLog;
  }

  private _fileMaps: [{ fileKey: string, fileLoc: string, log?: string }?] = [];

  get fileMaps(): [{ fileKey: string, fileLoc: string }?] {
    return this._fileMaps;
  }

  private _selFileKey: string;

  get selFileKey(): string {
    return this._selFileKey;
  }

  set selFileKey(value: string) {
    this._selFileKey = value;
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  ngOnDestroy(): void {
    this.testSub.unsubscribe();
  }

  ngOnInit() {
    this.testSub = this.stompService.watch("/topic/test")
      .subscribe(msg => {
        console.log(new Date(), msg);
      });

    console.log(`found config:\n${JSON.stringify(this.configService.env)}!`);

    // initialize filemaps
    Object.entries(this.configService.env.files)
      .forEach(entry => this._fileMaps.push(
        {fileKey: entry[0], fileLoc: entry[1].toString(), log: ""}));

    // subscription to keys
    this._fileMaps.forEach(fileMap => {
      return this.fileSubs.push(
        this.stompService.watch(`/topic/logs/${fileMap.fileKey}`)
          .subscribe(msg => {
            console.log(new Date(), msg.body);
            fileMap.log += `${msg.body}\n`;
            // check if also current, and update current
            if (this.curSelFileKey === fileMap.fileKey) this._curLog += `${msg.body}\n`;
          })
      );
    });


  }


  onSendTest() {
    this.stompService.publish(
      {
        destination: "/app/test",
        body: `Message sent at ${new Date()}`
      });
  }

  onFileKeyChange(fileKey: string) {
    this.curSelFileKey = fileKey;
    const filterElement = this._fileMaps.filter(fileMap => fileMap.fileKey === fileKey)[0];
    this._curLog = filterElement ? filterElement.log : "";
  }

  private scrollToBottom() {
    console.log('scroll!');
    const nativeElement = this.scrollMe.nativeElement;
    nativeElement.scrollTop = nativeElement.scrollHeight;
  }
}
