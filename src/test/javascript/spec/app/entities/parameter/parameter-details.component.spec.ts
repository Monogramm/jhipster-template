/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ParameterDetailComponent from '@/entities/parameter/parameter-details.vue';
import ParameterClass from '@/entities/parameter/parameter-details.component';
import ParameterService from '@/entities/parameter/parameter.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Parameter Management Detail Component', () => {
    let wrapper: Wrapper<ParameterClass>;
    let comp: ParameterClass;
    let parameterServiceStub: SinonStubbedInstance<ParameterService>;

    beforeEach(() => {
      parameterServiceStub = sinon.createStubInstance<ParameterService>(ParameterService);

      wrapper = shallowMount<ParameterClass>(ParameterDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { parameterService: () => parameterServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundParameter = { id: 123 };
        parameterServiceStub.find.resolves(foundParameter);

        // WHEN
        comp.retrieveParameter(123);
        await comp.$nextTick();

        // THEN
        expect(comp.parameter).toBe(foundParameter);
      });
    });
  });
});
