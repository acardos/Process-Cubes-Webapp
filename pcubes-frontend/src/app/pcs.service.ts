import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PCS} from './models/pcs';
import {ObjectUnsubscribedError, Observable, Subject} from 'rxjs';
import {DimensionAttribute} from './models/dimension-attribute';
import {Dimension} from './models/dimension';
import {VgroupDate} from './dimension-detail/attribute-detail/vgroup-date-form/vgroup-date';
import {VgroupNumber} from './dimension-detail/attribute-detail/vgroup-number-form/vgroup-number';
import {DimensionElement} from './dimension-detail/dimension-element';

@Injectable({
  providedIn: 'root'
})
export class PcsService {

  public pcss: PCS[];

  constructor(private http: HttpClient) {
    this.getAllPCS().subscribe(value => this.pcss = value);
  }

  getAllPCS(): Observable<PCS[]> {
    const observable = this.http.get<PCS[]>('/api/pcs/');
    const obSubject = new Subject<PCS[]>();
    observable.subscribe(value => {
      this.pcss = value;
      obSubject.next(value);
    });

    return obSubject.asObservable();
  }

  getPCS(id: number): Observable<PCS> {
    return this.http.get<PCS>(`/api/pcs/${id}`);
  }

  deletePCS(id: number) {
    return this.http.delete(`/api/pcs/${id}`);
  }

  createCubeStructure(name: string) {
    const observable = this.http.post<PCS>('/api/pcs/', {name});
    const obSubject = new Subject<PCS>();
    observable.subscribe(value => {
      this.pcss.push(value);
      obSubject.next(value);
    });

    return obSubject.asObservable();
  }

  addDimension(pcs: PCS, name: string): Observable<Dimension> {
    return this.http.post<Dimension>(`/api/pcs/${pcs.id}/dimensions`, new Dimension(name, []));
  }

  addAttribute(dimension: Dimension, attr: DimensionAttribute): Observable<DimensionAttribute> {
    console.log(attr);
    return this.http.post<DimensionAttribute>(`/api/dimensions/${dimension.id}/attributes/`, {name: attr.name, dtype: attr.dtype});
  }

  deleteDimension(dimension: Dimension) {
    return this.http.delete(`/api/dimensions/${dimension.id}`);
  }

  deleteAttribute(attribute: DimensionAttribute) {
    return this.http.delete(`/api/dimension_attribute/${attribute.id}/`);
  }

  getDimension(id: number) {
    return this.http.get<Dimension>(`/api/dimensions/${id}`);
  }

  addValueGroupDate(attributeId: number, vgroup: VgroupDate): Observable<VgroupDate> {

    console.log(vgroup.startDate.toString());
    console.log(vgroup.startDate.toDateString());
    console.log(vgroup.startDate.toISOString());
    console.log(vgroup.startDate.toTimeString());


    return this.http.post<VgroupDate>(`/api/dimension_attribute/${attributeId}/vgroup_date/`, {
      vgroup
    });
  }

  addValueGroupNumber(attributeId: number, vgroup: VgroupNumber): Observable<VgroupNumber> {
    console.log(vgroup);
    return this.http.post<VgroupNumber>(`/api/dimension_attribute/${attributeId}/vgroup_number/`, 
      vgroup
    );
  }

  addDimensionElement(dimension: Dimension, values: (string | number)[]) {
    console.log(values);
    return this.http.post<DimensionElement>(`/api/dimension/${dimension.id}/elements`, {dimension: dimension.id, values});
  }

}


