import { RestaurantService } from './restaurant-service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { CuisineListComponent } from './components/cuisine-list.component';
import { RestaurantCuisineComponent } from './components/restaurant-cuisine.component';
import { RestaurantDetailsComponent } from './components/restaurant-details.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [
  {path:'', component: CuisineListComponent},
  {path: ':cuisine/restaurants', component: RestaurantCuisineComponent},
  {path: 'restaurant/:id', component: RestaurantDetailsComponent},
  {path: '**', redirectTo:'/', pathMatch:'full'}
]
@NgModule({
  declarations: [
    AppComponent,
    CuisineListComponent,
    RestaurantCuisineComponent,
    RestaurantDetailsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes, {useHash:true}),
    ReactiveFormsModule
  ],
  providers: [RestaurantService],
  bootstrap: [AppComponent]
})
export class AppModule { }
