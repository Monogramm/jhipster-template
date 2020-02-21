import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class ParameterUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('templateApp.parameter.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  nameInput: ElementFinder = element(by.css('input#parameter-name'));

  descriptionInput: ElementFinder = element(by.css('input#parameter-description'));

  typeSelect = element(by.css('select#parameter-type'));

  valueInput: ElementFinder = element(by.css('input#parameter-value'));
}
