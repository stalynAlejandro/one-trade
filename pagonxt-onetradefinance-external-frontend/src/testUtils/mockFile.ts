import { FileProps } from '../types/FileProps';

interface MockFileProps {
  content?: string;
  documentType?: FileProps['documentType'];
  mimeType?: string;
  name?: string;
}

export const mockFile = (
  {
    name = 'file.pdf',
    content = 'my file content',
    mimeType = 'plain/text',
    documentType = undefined,
  }: MockFileProps = {} as MockFileProps,
) => {
  const contentBlob = new Blob([content], {
    type: mimeType,
  });

  const theFile: FileProps = new File([contentBlob], name, {
    lastModified: new Date().getTime(),
    type: mimeType,
  });

  theFile.uploadedDate = new Date().toJSON();
  if (documentType) {
    theFile.documentType = documentType;
  }

  return theFile;
};
