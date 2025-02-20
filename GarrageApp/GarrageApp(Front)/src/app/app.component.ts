import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { GarrageService } from './garrage.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [GarrageService]
})
export class AppComponent implements OnInit{
  title = 'GarrageApp';
  cars:Object

  constructor(private garrageService:GarrageService){ 

   }
  ngOnInit(){
    console.log("On init...")
    this.garrageService.getCars.subscribe((datas) =>{ 
      this.cars = datas; 
    } )
  }

}
