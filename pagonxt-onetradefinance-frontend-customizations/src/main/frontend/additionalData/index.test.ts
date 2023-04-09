import { additionalData } from './index';

describe('customProperties Utils', () => {
    const customPropertiesUtils = additionalData.custom;
    describe('uuid', () => {
        it('is possible to get a uuid', () => {
            expect(customPropertiesUtils.uuid()).toBeTruthy();
        });
    });

    describe('setFieldToObjectArray', () => {
        it('should change array in place', () => {
            const myArray = [
                { field: false, otherField: false },
                { field: true, otherField: true },
                { otherField: false },
            ];
            customPropertiesUtils.setFieldToObjectArray(myArray, 'field', true);
            const expected = [
                { field: true, otherField: false },
                { field: true, otherField: true },
                { field: true, otherField: false },
            ];
            expect(myArray).toEqual(expected);
        });
    });
});
