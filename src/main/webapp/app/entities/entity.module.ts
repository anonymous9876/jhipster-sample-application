import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'batch',
        loadChildren: () => import('./batch/batch.module').then(m => m.JhipsterSampleApplicationBatchModule)
      },
      {
        path: 'request',
        loadChildren: () => import('./request/request.module').then(m => m.JhipsterSampleApplicationRequestModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class JhipsterSampleApplicationEntityModule {}
