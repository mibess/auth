import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor() { }

  showMessage(message: string) {
    alert(message);
  }

  showListMessage(e: any){
    let errors = e.error.errors;
    for (let key in errors) {
      alert(errors[key]);
    }
  }

}
