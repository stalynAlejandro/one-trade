import * as React from 'react';
import StorybookForm from '../../util/storybookForm';

export const Pieces = () => (
    <StorybookForm
        payload={{
            pieces: 12456,
        }}
        sfs={`
        number: value={{pieces}} size=6 label=pieces col=0 [[numberType=integer fractionSize=0 type=one]]
        customPiecesCounter: value={{pieces}} size=6 label=Number of Pieces Produced col=6
        `}
    />
);

export default {
    title: 'pieces counter component',
};
