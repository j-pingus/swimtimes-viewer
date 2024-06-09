import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ClubComponent} from "./page/club/club.component";
import {AthleteComponent} from "./page/athlete/athlete.component";

const routes: Routes = [
  {
    path: 'club/:id',
    component: ClubComponent
  },
  {
    path: 'athlete/:id',
    component: AthleteComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
