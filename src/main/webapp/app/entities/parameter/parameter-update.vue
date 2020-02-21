<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="templateApp.parameter.home.createOrEditLabel" v-text="$t('templateApp.parameter.home.createOrEditLabel')">Create or edit a Parameter</h2>
                <div>
                    <div class="form-group" v-if="parameter.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="parameter.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('templateApp.parameter.name')" for="parameter-name">Name</label>
                        <input type="text" class="form-control" name="name" id="parameter-name"
                            :class="{'valid': !$v.parameter.name.$invalid, 'invalid': $v.parameter.name.$invalid }" v-model="$v.parameter.name.$model"  required/>
                        <div v-if="$v.parameter.name.$anyDirty && $v.parameter.name.$invalid">
                            <small class="form-text text-danger" v-if="!$v.parameter.name.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.parameter.name.maxLength" v-text="$t('entity.validation.maxlength', {max: 256})">
                                This field cannot be longer than 256 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('templateApp.parameter.description')" for="parameter-description">Description</label>
                        <input type="text" class="form-control" name="description" id="parameter-description"
                            :class="{'valid': !$v.parameter.description.$invalid, 'invalid': $v.parameter.description.$invalid }" v-model="$v.parameter.description.$model" />
                        <div v-if="$v.parameter.description.$anyDirty && $v.parameter.description.$invalid">
                            <small class="form-text text-danger" v-if="!$v.parameter.description.maxLength" v-text="$t('entity.validation.maxlength', {max: 4096})">
                                This field cannot be longer than 4096 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('templateApp.parameter.type')" for="parameter-type">Type</label>
                        <select class="form-control" name="type" :class="{'valid': !$v.parameter.type.$invalid, 'invalid': $v.parameter.type.$invalid }" v-model="$v.parameter.type.$model" id="parameter-type"  required>
                            <option value="URL" v-bind:label="$t('templateApp.ParameterType.URL')">URL</option>
                            <option value="PATH" v-bind:label="$t('templateApp.ParameterType.PATH')">PATH</option>
                            <option value="COLOR" v-bind:label="$t('templateApp.ParameterType.COLOR')">COLOR</option>
                            <option value="DATE_TIME" v-bind:label="$t('templateApp.ParameterType.DATE_TIME')">DATE_TIME</option>
                            <option value="TIME" v-bind:label="$t('templateApp.ParameterType.TIME')">TIME</option>
                            <option value="DATE" v-bind:label="$t('templateApp.ParameterType.DATE')">DATE</option>
                            <option value="DOUBLE" v-bind:label="$t('templateApp.ParameterType.DOUBLE')">DOUBLE</option>
                            <option value="INTEGER" v-bind:label="$t('templateApp.ParameterType.INTEGER')">INTEGER</option>
                            <option value="BOOLEAN" v-bind:label="$t('templateApp.ParameterType.BOOLEAN')">BOOLEAN</option>
                            <option value="STRING" v-bind:label="$t('templateApp.ParameterType.STRING')">STRING</option>
                            <option value="PASSWORD" v-bind:label="$t('templateApp.ParameterType.PASSWORD')">PASSWORD</option>
                            <option value="ANY" v-bind:label="$t('templateApp.ParameterType.ANY')">ANY</option>
                        </select>
                        <div v-if="$v.parameter.type.$anyDirty && $v.parameter.type.$invalid">
                            <small class="form-text text-danger" v-if="!$v.parameter.type.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('templateApp.parameter.value')" for="parameter-value">Value</label>
                        <input type="text" class="form-control" name="value" id="parameter-value"
                            :class="{'valid': !$v.parameter.value.$invalid, 'invalid': $v.parameter.value.$invalid }" v-model="$v.parameter.value.$model"  required/>
                        <div v-if="$v.parameter.value.$anyDirty && $v.parameter.value.$invalid">
                            <small class="form-text text-danger" v-if="!$v.parameter.value.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.parameter.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./parameter-update.component.ts">
</script>
