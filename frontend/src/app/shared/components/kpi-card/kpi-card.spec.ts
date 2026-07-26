import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KpiCard } from './kpi-card';

describe('KpiCard', () => {
  let component: KpiCard;
  let fixture: ComponentFixture<KpiCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KpiCard],
    }).compileComponents();

    fixture = TestBed.createComponent(KpiCard);
    fixture.componentRef.setInput('titulo', 'Total de KM');
    fixture.componentRef.setInput('valor', '1.455,50 km');
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the titulo and valor inputs', () => {
    const texto = fixture.nativeElement.textContent;
    expect(texto).toContain('Total de KM');
    expect(texto).toContain('1.455,50 km');
  });
});
