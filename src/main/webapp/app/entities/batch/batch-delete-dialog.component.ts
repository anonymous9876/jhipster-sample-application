import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBatch } from 'app/shared/model/batch.model';
import { BatchService } from './batch.service';

@Component({
  templateUrl: './batch-delete-dialog.component.html'
})
export class BatchDeleteDialogComponent {
  batch: IBatch;

  constructor(protected batchService: BatchService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.batchService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'batchListModification',
        content: 'Deleted an batch'
      });
      this.activeModal.dismiss(true);
    });
  }
}
