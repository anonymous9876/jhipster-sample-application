import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRequest } from 'app/shared/model/request.model';
import { RequestService } from './request.service';

@Component({
  templateUrl: './request-delete-dialog.component.html'
})
export class RequestDeleteDialogComponent {
  request: IRequest;

  constructor(protected requestService: RequestService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.requestService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'requestListModification',
        content: 'Deleted an request'
      });
      this.activeModal.dismiss(true);
    });
  }
}
