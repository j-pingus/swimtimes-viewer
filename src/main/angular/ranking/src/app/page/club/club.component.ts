import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ReferencesService} from "../../services/references.service";
import {Observable} from "rxjs";
import {Athlete} from "../../domain/Athlete";

@Component({
  selector: 'app-club',
  templateUrl: './club.component.html',
  styleUrls: ['./club.component.css']
})
export class ClubComponent implements OnInit {
  clubId: number = 0;
  athletes: Observable<Array<Athlete>> = new Observable<Array<Athlete>>();

  constructor(route: ActivatedRoute, referenceService: ReferencesService, private router: Router) {
    route.params.subscribe(params => {
      this.clubId = +params['id']; // (+) converts string 'id' to a number
      this.athletes = referenceService.getClubAthletes(this.clubId);
    });
  }

  ngOnInit(): void {
  }

  go(id: number) {
    this.router.navigate(['athlete', id]);
  }
}
