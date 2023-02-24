import { RestaurantService } from './../restaurant-service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit{
  cuisines: string[] = [];
  constructor(private restaurantSvc: RestaurantService){}

  ngOnInit(): void {
    this.restaurantSvc.getCuisineList()
      .then((response:any) => {
        this.cuisines = response['cuisines'];
      })
      .catch(error => console.log(error))
  }




}
