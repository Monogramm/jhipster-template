import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IParameter } from '@/shared/model/parameter.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import ParameterService from './parameter.service';

@Component
export default class Parameter extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('parameterService') private parameterService: () => ParameterService;
  public currentSearch = '';
  private removeId: number = null;
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;
  public parameters: IParameter[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllParameters();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllParameters();
  }

  public clear(): void {
    this.currentSearch = '';
    this.page = 1;
    this.retrieveAllParameters();
  }

  public retrieveAllParameters(): void {
    this.isFetching = true;

    const paginationQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    };
    if (this.currentSearch) {
      this.parameterService()
        .search(this.currentSearch, paginationQuery)
        .then(
          res => {
            this.parameters = res.data;
            this.totalItems = Number(res.headers['x-total-count']);
            this.queryCount = this.totalItems;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.parameterService()
      .retrieve(paginationQuery)
      .then(
        res => {
          this.parameters = res.data;
          this.totalItems = Number(res.headers['x-total-count']);
          this.queryCount = this.totalItems;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IParameter): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeParameter(): void {
    this.parameterService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('templateApp.parameter.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();

        this.removeId = null;
        this.retrieveAllParameters();
        this.closeDialog();
      });
  }

  public sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.retrieveAllParameters();
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
