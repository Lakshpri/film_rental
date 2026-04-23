import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FilmTextListComponent } from './film-text-list-list.component';

describe('FilmTextListComponent', () => {
  let component: FilmTextListComponent;
  let fixture: ComponentFixture<FilmTextListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FilmTextListComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FilmTextListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
