import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './home/home.component';
import {HttpClientModule} from "@angular/common/http";
import {MatButtonModule} from "@angular/material/button";
import { ClubComponent } from './page/club/club.component';
import { AthleteComponent } from './page/athlete/athlete.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import { SwimrankingNumberComponent } from './dialog/swimranking-number/swimranking-number.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import { TimeDetailsComponent } from './dialog/time-details/time-details.component';
import {MatChipsModule} from "@angular/material/chips";
import {MatIconModule} from "@angular/material/icon";
import { CompareComponent } from './page/compare/compare.component';
import { PointsDetailsComponent } from './dialog/points-details/points-details.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ClubComponent,
    AthleteComponent,
    SwimrankingNumberComponent,
    TimeDetailsComponent,
    CompareComponent,
    PointsDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatButtonModule,
    MatTooltipModule,
    MatDialogModule,
    MatInputModule,
    FormsModule,
    MatChipsModule,
    MatIconModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
