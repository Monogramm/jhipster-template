import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';
import { IParameter, Parameter } from '@/shared/model/parameter.model';
import ParameterService from './parameter.service';

const validations: any = {
  parameter: {
    name: {
      required,
      maxLength: maxLength(256)
    },
    description: {
      maxLength: maxLength(4096)
    },
    type: {
      required
    },
    value: {
      required
    }
  }
};

@Component({
  validations
})
export default class ParameterUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('parameterService') private parameterService: () => ParameterService;
  public parameter: IParameter = new Parameter();
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.parameterId) {
        vm.retrieveParameter(to.params.parameterId);
      }
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.parameter.id) {
      this.parameterService()
        .update(this.parameter)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('templateApp.parameter.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.parameterService()
        .create(this.parameter)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('templateApp.parameter.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveParameter(parameterId): void {
    this.parameterService()
      .find(parameterId)
      .then(res => {
        this.parameter = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
