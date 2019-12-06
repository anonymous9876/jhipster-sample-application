import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IRequest, Request } from 'app/shared/model/request.model';
import { RequestService } from './request.service';

@Component({
  selector: 'jhi-request-update',
  templateUrl: './request-update.component.html'
})
export class RequestUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    reference: []
  });

  constructor(protected requestService: RequestService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ request }) => {
      this.updateForm(request);
    });
  }

  updateForm(request: IRequest) {
    this.editForm.patchValue({
      id: request.id,
      reference: request.reference
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const request = this.createFromForm();
    if (request.id !== undefined) {
      this.subscribeToSaveResponse(this.requestService.update(request));
    } else {
      this.subscribeToSaveResponse(this.requestService.create(request));
    }
  }

  private createFromForm(): IRequest {
    return {
      ...new Request(),
      id: this.editForm.get(['id']).value,
      reference: this.editForm.get(['reference']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequest>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
