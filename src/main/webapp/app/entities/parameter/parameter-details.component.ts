import { Component, Vue, Inject } from 'vue-property-decorator';

import { IParameter } from '@/shared/model/parameter.model';
import ParameterService from './parameter.service';

@Component
export default class ParameterDetails extends Vue {
  @Inject('parameterService') private parameterService: () => ParameterService;
  public parameter: IParameter = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.parameterId) {
        vm.retrieveParameter(to.params.parameterId);
      }
    });
  }

  public retrieveParameter(parameterId) {
    this.parameterService()
      .find(parameterId)
      .then(res => {
        this.parameter = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
