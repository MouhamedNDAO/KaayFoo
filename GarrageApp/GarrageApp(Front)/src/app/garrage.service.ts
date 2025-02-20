import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GarrageService {
 readonly API_URL = "http://localhost/4200"
 readonly ENDPOINT_CARS = "/cars"

  constructor(private httpClient:HttpClient) { }

  getCars(){ 
    return this.httpClient.get(this.API_URL +this.ENDPOINT_CARS)
   }
}
