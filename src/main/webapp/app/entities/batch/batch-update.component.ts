import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IBatch, Batch } from 'app/shared/model/batch.model';
import { BatchService } from './batch.service';

@Component({
  selector: 'jhi-batch-update',
  templateUrl: './batch-update.component.html'
})
export class BatchUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    reference: []
  });

  constructor(protected batchService: BatchService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ batch }) => {
      this.updateForm(batch);
    });
  }

  updateForm(batch: IBatch) {
    this.editForm.patchValue({
      id: batch.id,
      reference: batch.reference
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const batch = this.createFromForm();
    if (batch.id !== undefined) {
      this.subscribeToSaveResponse(this.batchService.update(batch));
    } else {
      this.subscribeToSaveResponse(this.batchService.create(batch));
    }
  }

  private createFromForm(): IBatch {
    return {
      ...new Batch(),
      id: this.editForm.get(['id']).value,
      reference: this.editForm.get(['reference']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBatch>>) {
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
